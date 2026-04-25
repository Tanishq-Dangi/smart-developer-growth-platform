import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { signup } from "../api/platformApi";

const initialForm = {
  name: "",
  email: "",
  password: "",
  goal: "Backend",
  level: "Beginner"
};

function Signup() {
  const navigate = useNavigate();
  const [form, setForm] = useState(initialForm);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  function getErrorMessage(error) {
    return (
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      error?.message ||
      "Signup failed."
    );
  }

  function handleInputChange(event) {
    const { name, value } = event.target;
    setForm((previous) => ({
      ...previous,
      [name]: value
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setErrorMessage("");
    setIsSubmitting(true);

    try {
      await signup(form);
      navigate("/login", {
        replace: true,
        state: { message: "Signup successful. Please log in." }
      });
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
        <h1 className="mt-3 font-display text-2xl text-slateBlue-900">Signup</h1>
        <p className="mt-1 text-sm text-slate-500">Create a new account to start your growth tracking.</p>

        <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
          <label className="block text-sm font-semibold text-slateBlue-900">
            Name
            <input
              className="mt-1 w-full rounded-xl border border-slateBlue-100 px-3 py-2 text-sm outline-none transition focus:border-slateBlue-500 focus:ring-2 focus:ring-slateBlue-100"
              name="name"
              value={form.name}
              onChange={handleInputChange}
              placeholder="Jane Developer"
              required
            />
          </label>

          <label className="block text-sm font-semibold text-slateBlue-900">
            Email
            <input
              type="email"
              className="mt-1 w-full rounded-xl border border-slateBlue-100 px-3 py-2 text-sm outline-none transition focus:border-slateBlue-500 focus:ring-2 focus:ring-slateBlue-100"
              name="email"
              value={form.email}
              onChange={handleInputChange}
              placeholder="jane@example.com"
              required
            />
          </label>

          <label className="block text-sm font-semibold text-slateBlue-900">
            Password
            <input
              type="password"
              className="mt-1 w-full rounded-xl border border-slateBlue-100 px-3 py-2 text-sm outline-none transition focus:border-slateBlue-500 focus:ring-2 focus:ring-slateBlue-100"
              name="password"
              value={form.password}
              onChange={handleInputChange}
              placeholder="Create password"
              required
            />
          </label>

          <label className="block text-sm font-semibold text-slateBlue-900">
            Goal
            <select
              className="mt-1 w-full rounded-xl border border-slateBlue-100 bg-white px-3 py-2 text-sm outline-none transition focus:border-slateBlue-500 focus:ring-2 focus:ring-slateBlue-100"
              name="goal"
              value={form.goal}
              onChange={handleInputChange}
            >
              <option value="Backend">Backend</option>
              <option value="DSA">DSA</option>
              <option value="System Design">System Design</option>
            </select>
          </label>

          <label className="block text-sm font-semibold text-slateBlue-900">
            Level
            <select
              className="mt-1 w-full rounded-xl border border-slateBlue-100 bg-white px-3 py-2 text-sm outline-none transition focus:border-slateBlue-500 focus:ring-2 focus:ring-slateBlue-100"
              name="level"
              value={form.level}
              onChange={handleInputChange}
            >
              <option value="Beginner">Beginner</option>
              <option value="Intermediate">Intermediate</option>
              <option value="Advanced">Advanced</option>
            </select>
          </label>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full rounded-xl bg-slateBlue-700 px-4 py-2.5 font-semibold text-white transition hover:bg-slateBlue-900 disabled:cursor-not-allowed disabled:opacity-60"
          >
            {isSubmitting ? "Creating..." : "Create Account"}
          </button>
        </form>

        {errorMessage && (
          <p className="mt-4 rounded-xl bg-red-50 px-3 py-2 text-sm font-semibold text-red-700">
            {errorMessage}
          </p>
        )}

        <p className="mt-5 text-sm text-slate-600">
          Already have an account?{" "}
          <Link className="font-semibold text-slateBlue-800 hover:text-slateBlue-900" to="/login">
            Login
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Signup;
