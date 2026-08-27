export type Difficulty = 'EASY' | 'MEDIUM' | 'HARD';
export type SectionType = 'BEGIN' | 'NODE' | 'END' | 'GAME_OVER';
export type ConsequenceType = 'LOSE_HEALTH' | 'GAIN_HEALTH';

export interface Book {
  id: string;
  title: string;
  author: string;
  difficulty: Difficulty;
  genres: string[];
}

export interface Consequence {
  type: ConsequenceType;
  value: number;
  text: string;
}

export interface GameOption {
  description: string;
  gotoId: number;
  consequence: Consequence | null;
}

export interface Section {
  id: number;
  text: string;
  type: SectionType;
  options: GameOption[];
}

export interface BookDetail extends Book {
  sections: Section[];
}

export interface Progress {
  currentSectionId: number;
  health: number;
  updatedAt: string;
}
