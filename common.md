export enum Roles {
  ADMIN = "admin",
  MODERATOR = "moderator",
  USER = "user",
}

export enum Status {
  ACTIVE = "active",
  INACTIVE = "inactive",
}

export enum TransactionStatus {
  ACTIVE = "active",
  EXPIRED = "expired",
  CANCELLED = "cancelled",
  REFUNDED = "refunded",
}

export interface User {
  id: string;
  name: string;
  email: string;
  avatar: string;
  status?: Status;
  createdAt: string;
  role: Roles;
}

export interface RentedFilm {
  id: string;
  title: string;
  poster: string;
  rentedAt: string;
  expiresAt: string;
  price: string;
  status: "active" | "expired";
  watchTime: number;
  totalDuration: number;
}

export interface Category {
  id: string;
  name: string;
  imageUrl: string;
  filmCount: number;
}

export interface Film {
  id: string;
  title: string;
  director: string;
  year: number;
  genre: { id: string; name: string }[];
  posterUrl: string;
  duration: string;
  isPremium: boolean;
  synopsis: string;
}

export interface EnhancedFilm {
  id: string;
  title: string;
  director: string;
  year: number;
  state: string;
  category: string;
  genres: { id: string; name: string }[];
  originalLanguage: string;
  subtitles: string[];
  format: string;
  color: string;
  duration: string;
  ageRating: string;
  posterUrl: string;
  wallpaperUrl?: string;
  filmUrl: string;
  trailerUrl?: string;
  rentalPrice: string;
  synopsis: string;
  isPremium?: boolean;
}

export interface Transaction {
  id: string;
  user: string;
  film: string;
  amount: string;
  date: string;
  status: TransactionStatus;
}
