import { Component, OnInit, computed, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BookDetail, Consequence, GameOption, Progress, Section } from '../../models/book.model';
import { BookService } from '../../services/book.service';
import { ProgressService } from '../../services/progress.service';

const MAX_HEALTH = 10;

@Component({
  selector: 'app-game',
  imports: [RouterLink],
  templateUrl: './game.html',
  styleUrl: './game.scss',
})
export class Game implements OnInit {
  protected readonly maxHealth = MAX_HEALTH;

  protected readonly book = signal<BookDetail | null>(null);
  protected readonly loading = signal(true);
  protected readonly error = signal<string | null>(null);

  protected readonly currentSectionId = signal<number | null>(null);
  protected readonly health = signal(MAX_HEALTH);
  protected readonly lastConsequence = signal<Consequence | null>(null);

  protected readonly resumeAvailable = signal<Progress | null>(null);
  protected readonly saveStatus = signal<'idle' | 'saving' | 'saved' | 'error'>('idle');

  protected readonly currentSection = computed<Section | null>(() => {
    const book = this.book();
    const sectionId = this.currentSectionId();
    if (!book || sectionId === null) {
      return null;
    }
    return book.sections.find((section) => section.id === sectionId) ?? null;
  });

  protected readonly isDead = computed(() => this.currentSectionId() !== null && this.health() <= 0);

  protected readonly isEnding = computed(() => this.currentSection()?.type === 'END');

  protected readonly isGameOver = computed(() => {
    const sectionId = this.currentSectionId();
    if (sectionId === null) {
      return false;
    }
    const section = this.currentSection();
    if (!section) {
      return true;
    }
    return section.type === 'GAME_OVER' || (section.type !== 'END' && section.options.length === 0);
  });

  protected readonly isGameFinished = computed(() => this.isDead() || this.isEnding() || this.isGameOver());

  constructor(
    private readonly route: ActivatedRoute,
    private readonly bookService: BookService,
    private readonly progressService: ProgressService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.error.set('No book selected.');
      this.loading.set(false);
      return;
    }

    this.bookService.getBook(id).subscribe({
      next: (book) => {
        this.book.set(book);
        this.loading.set(false);
        this.progressService.get(id).subscribe({
          next: (progress) => {
            if (progress) {
              this.resumeAvailable.set(progress);
            } else {
              this.currentSectionId.set(this.findBeginSectionId(book));
            }
          },
          error: () => this.currentSectionId.set(this.findBeginSectionId(book)),
        });
      },
      error: () => {
        this.error.set('Could not load this adventure. Is the backend running?');
        this.loading.set(false);
      },
    });
  }

  protected choose(option: GameOption): void {
    if (option.consequence) {
      const delta = option.consequence.type === 'LOSE_HEALTH' ? -option.consequence.value : option.consequence.value;
      this.health.set(Math.max(0, Math.min(MAX_HEALTH, this.health() + delta)));
      this.lastConsequence.set(option.consequence);
    } else {
      this.lastConsequence.set(null);
    }
    this.currentSectionId.set(option.gotoId);
    this.clearSaveIfGameFinished();
  }

  protected resumeGame(): void {
    const progress = this.resumeAvailable();
    if (!progress) {
      return;
    }
    this.health.set(progress.health);
    this.currentSectionId.set(progress.currentSectionId);
    this.resumeAvailable.set(null);
  }

  protected startOver(): void {
    const book = this.book();
    if (!book) {
      return;
    }
    this.health.set(MAX_HEALTH);
    this.currentSectionId.set(this.findBeginSectionId(book));
    this.resumeAvailable.set(null);
  }

  protected restart(): void {
    const book = this.book();
    if (!book) {
      return;
    }
    this.health.set(MAX_HEALTH);
    this.lastConsequence.set(null);
    this.currentSectionId.set(this.findBeginSectionId(book));
  }

  protected saveProgress(): void {
    const book = this.book();
    const sectionId = this.currentSectionId();
    if (!book || sectionId === null) {
      return;
    }
    this.saveStatus.set('saving');
    this.progressService.save(book.id, sectionId, this.health()).subscribe({
      next: () => this.flashSaveStatus('saved'),
      error: () => this.flashSaveStatus('error'),
    });
  }

  private flashSaveStatus(status: 'saved' | 'error'): void {
    this.saveStatus.set(status);
    setTimeout(() => this.saveStatus.set('idle'), 2000);
  }

  private clearSaveIfGameFinished(): void {
    const book = this.book();
    if (book && this.isGameFinished()) {
      this.progressService.clear(book.id).subscribe({ error: () => {} });
    }
  }

  private findBeginSectionId(book: BookDetail): number {
    return book.sections.find((section) => section.type === 'BEGIN')!.id;
  }
}
