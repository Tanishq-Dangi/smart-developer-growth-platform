import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { baseURL } from "../api/client";
import { login } from "../api/platformApi";
import { setAuthData } from "../auth/storage";

function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const signupMessage = location.state?.message ?? "";

  function getErrorMessage(error) {
    return (
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      error?.message ||
      "Login failed."
    );
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setErrorMessage("");
    setIsSubmitting(true);

    try {
      const authResponse = await login({ email, password });
      setAuthData(authResponse);
      navigate("/dashboard", { replace: true });
    } catch (error) {
      setErrorMessage(getErrorMessage(error));
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="mx-auto flex min-h-screen max-w-7xl items-center px-4 py-8 sm:px-6 lg:px-8">
      <div className="rise mx-auto w-full max-w-md rounded-3xl bg-white p-7 shadow-soft">
        <p className="font-display text-xs uppercase tracking-[0.2em] text-slateBlue-700">
          Smart Developer Growth Platform
        </p>
        <h1 className="mt-3 font-display text-2xl text-slateBlue-900">Login</h1>
        <p className="mt-1 text-sm text-slate-500">Authenticate to access your personalized dashboard.</p>

        <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
          <label className="block text-sm font-semibold text-slateBlue-900">
            Email
            <input
              type="email"
              className="mt-1 w-full rounded-xl border border-slateBlue-100 px-3 py-2 text-sm outline-none transition focus:border-slateBlue-500 focus:ring-2 focus:ring-slateBlue-100"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              placeholder="jane@example.com"
              required
            />
          </label>

          <label className="block text-sm font-semibold text-slateBlue-900">
            Password
            <input
              type="password"
              className="mt-1 w-full rounded-xl border border-slateBlue-100 px-3 py-2 text-sm outline-none transition focus:border-slateBlue-500 focus:ring-2 focus:ring-slateBlue-100"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="Enter password"
              required
            />
          </label>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full rounded-xl bg-slateBlue-700 px-4 py-2.5 font-semibold text-white transition hover:bg-slateBlue-900 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {isSubmitting ? "Signing in..." : "Login"}
          </button>
        </form>

        {signupMessage && (
          <p className="mt-4 rounded-xl bg-mint-100 px-3 py-2 text-sm font-semibold text-emerald-800">
            {signupMessage}
          </p>
        )}
        {errorMessage && (
          <p className="mt-4 rounded-xl bg-red-50 px-3 py-2 text-sm font-semibold text-red-700">
            {errorMessage}
          </p>
        )}

        <p className="mt-5 text-sm text-slate-600">
          New user?{" "}
          <Link className="font-semibold text-slateBlue-800 hover:text-slateBlue-900" to="/signup">
            Create account
          </Link>
        </p>
        <p className="mt-3 rounded-full bg-slateBlue-50 px-3 py-2 text-xs font-semibold text-slateBlue-700">
          API Base URL: {baseURL}
        </p>
      </div>
    </div>
  );
}

export default Login;
