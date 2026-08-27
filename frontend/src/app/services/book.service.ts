import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book, BookDetail } from '../models/book.model';

@Injectable({ providedIn: 'root' })
export class BookService {
  private readonly apiUrl = 'http://localhost:8081/api/books';

  constructor(private readonly http: HttpClient) {}

  getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.apiUrl);
  }

  getBook(id: string): Observable<BookDetail> {
    return this.http.get<BookDetail>(`${this.apiUrl}/${id}`);
  }
}
