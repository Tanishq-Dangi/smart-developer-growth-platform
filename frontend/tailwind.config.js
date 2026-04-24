/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      fontFamily: {
        display: ["Space Grotesk", "sans-serif"],
        body: ["Manrope", "sans-serif"]
      },
      colors: {
        slateBlue: {
          50: "#f4f6ff",
          100: "#e9edff",
          200: "#d0daff",
          500: "#5f6fe8",
          700: "#3947b7",
          900: "#1e255f"
        },
        mint: {
          100: "#dbfff2",
          300: "#81e8c1",
          500: "#2ec58f"
        },
        amberSoft: {
          100: "#fff1d2",
          500: "#ffb547"
        }
      },
      boxShadow: {
        soft: "0 12px 40px rgba(25, 35, 90, 0.12)"
      }
    }
  },
  plugins: []
};
