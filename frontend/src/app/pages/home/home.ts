import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TitleCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Book, Difficulty } from '../../models/book.model';
import { BookService } from '../../services/book.service';

type DifficultyFilter = Difficulty | 'ALL';

@Component({
  selector: 'app-home',
  imports: [FormsModule, TitleCasePipe, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  protected readonly books = signal<Book[]>([]);
  protected readonly loading = signal(true);
  protected readonly error = signal<string | null>(null);

  protected readonly searchTerm = signal('');
  protected readonly selectedDifficulty = signal<DifficultyFilter>('ALL');
  protected readonly selectedGenres = signal<ReadonlySet<string>>(new Set());

  protected readonly difficultyFilters: DifficultyFilter[] = ['ALL', 'EASY', 'MEDIUM', 'HARD'];

  protected readonly availableGenres = computed(() =>
    [...new Set(this.books().flatMap((book) => book.genres))].sort()
  );

  protected readonly filteredBooks = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    const difficulty = this.selectedDifficulty();
    const genres = this.selectedGenres();

    return this.books().filter((book) => {
      const matchesTerm =
        term.length === 0 ||
        book.title.toLowerCase().includes(term) ||
        book.author.toLowerCase().includes(term);
      const matchesDifficulty = difficulty === 'ALL' || book.difficulty === difficulty;
      const matchesGenres = genres.size === 0 || book.genres.some((genre) => genres.has(genre));
      return matchesTerm && matchesDifficulty && matchesGenres;
    });
  });

  constructor(private readonly bookService: BookService) {}

  ngOnInit(): void {
    this.bookService.getAllBooks().subscribe({
      next: (books) => {
        this.books.set(books);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load the adventure library. Is the backend running?');
        this.loading.set(false);
      },
    });
  }

  protected selectDifficulty(difficulty: DifficultyFilter): void {
    this.selectedDifficulty.set(difficulty);
  }

  protected toggleGenre(genre: string): void {
    const next = new Set(this.selectedGenres());
    if (next.has(genre)) {
      next.delete(genre);
    } else {
      next.add(genre);
    }
    this.selectedGenres.set(next);
  }
}
