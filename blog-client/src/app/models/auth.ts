export interface AuthRequest {
  email: string,
  password: string
}

export interface AuthResponse {
  username: string,
  access_token: string,
  refresh_token: string
}