import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Progress } from '../models/book.model';

@Injectable({ providedIn: 'root' })
export class ProgressService {
  private readonly apiUrl = 'http://localhost:8081/api/books';

  constructor(private readonly http: HttpClient) {}

  get(bookId: string): Observable<Progress | null> {
    return this.http.get<Progress | null>(`${this.apiUrl}/${bookId}/progress`);
  }

  save(bookId: string, currentSectionId: number, health: number): Observable<Progress> {
    return this.http.put<Progress>(`${this.apiUrl}/${bookId}/progress`, { currentSectionId, health });
  }

  clear(bookId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${bookId}/progress`);
  }
}
