export type Difficulty = 'EASY' | 'MEDIUM' | 'HARD';

export interface Book {
  id: string;
  title: string;
  author: string;
  difficulty: Difficulty;
  genres: string[];
}
