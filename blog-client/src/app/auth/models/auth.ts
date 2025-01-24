export interface AuthRequest {
  email: string,
  password: string
}

export interface AuthResponse {
  username: string,
  accessToken: string,
  refreshToken: string
}