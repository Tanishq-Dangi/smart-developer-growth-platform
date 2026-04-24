# Smart Developer Growth Frontend

## Setup

1. Install dependencies:
   ```bash
   npm install
   ```
2. Create `.env` from `.env.example` and set backend URL:
   ```env
   VITE_API_BASE_URL=/api
   VITE_PROXY_TARGET=http://localhost:8080
   ```
3. Start dev server:
   ```bash
   npm run dev
   ```

## Build

```bash
npm run build
```

## Notes

- User creation sends a placeholder password (`Temp@12345`) because the current backend requires `password` in `/users`.
- All dashboard sections use Axios and call the backend APIs directly.
- Default local setup uses Vite proxy (`/api`) to avoid browser CORS issues.
