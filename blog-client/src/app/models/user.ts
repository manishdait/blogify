export interface UserResponse {
  first_name: string,
  last_name: string,
  email: string,
  img_url: string | null
}

export interface UserRequest {
  first_name: string,
  last_name: string
}