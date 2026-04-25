import { useEffect, useState, useTransition } from "react";
import { useNavigate } from "react-router-dom";
import { baseURL } from "../api/client";
import {
  completeTask,
  generateTasks,
  getInsight,
  getProgress,
  getTasks,
  getWeakness
} from "../api/platformApi";
import { clearAuthData, getStoredUser } from "../auth/storage";

function Dashboard() {
  const navigate = useNavigate();
  const [user] = useState(() => getStoredUser());

  const [tasks, setTasks] = useState([]);
  const [progress, setProgress] = useState(null);
  const [weakness, setWeakness] = useState({});
  const [insight, setInsight] = useState(null);

  const [isGeneratingTasks, setIsGeneratingTasks] = useState(false);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [completingTaskId, setCompletingTaskId] = useState(null);
  const [isPending, startTransition] = useTransition();

  const [statusMessage, setStatusMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  function getErrorMessage(error) {
    return (
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      error?.message ||
      "Request failed."
    );
  }

  async function refreshDashboard() {
    if (!user?.email) {
      return;
    }

    setErrorMessage("");
    setIsRefreshing(true);
    try {
      const [tasksData, progressData, weaknessData, insightData] = await Promise.all([
        getTasks(),
        getProgress(),
        getWeakness(),
        getInsight()
      ]);

      startTransition(() => {
        setTasks(tasksData);
        setProgress(progressData);
        setWeakness(weaknessData);
        setInsight(insightData);
      });
    } catch (error) {
      setErrorMessage(getErrorMessage(error));
    } finally {
      setIsRefreshing(false);
    }
  }

  useEffect(() => {
    if (!user?.email) {
      clearAuthData();
      navigate("/login", { replace: true });
      return;
    }
    refreshDashboard();
  }, [navigate, user?.email]);

  async function handleGenerateTasks() {
    if (!user?.email) {
      return;
    }

    setErrorMessage("");
    setStatusMessage("");
    setIsGeneratingTasks(true);

    try {
      const generatedTasks = await generateTasks();
      if (generatedTasks.length === 0) {
        setStatusMessage("No new tasks generated. Today's recommendations already exist.");
      } else {
        setStatusMessage(`${generatedTasks.length} tasks generated successfully.`);
      }
      await refreshDashboard();
    } catch (error) {
      setErrorMessage(getErrorMessage(error));
    } finally {
      setIsGeneratingTasks(false);
    }
  }

  async function handleMarkTaskAsDone(taskId) {
    setErrorMessage("");
    setStatusMessage("");
    setCompletingTaskId(taskId);

    try {
      const updatedTask = await completeTask(taskId);
      setStatusMessage(`Task marked as done: ${updatedTask.title}`);
      await refreshDashboard();
    } catch (error) {
      setErrorMessage(getErrorMessage(error));
    } finally {
      setCompletingTaskId(null);
    }
  }

  function handleLogout() {
    clearAuthData();
    navigate("/login", { replace: true });
  }

  const weaknessEntries = Object.entries(weakness);

  return (
    <div className="mx-auto max-w-7xl px-4 pb-10 pt-8 sm:px-6 lg:px-8">
      <header className="rise mb-8 rounded-3xl bg-white/80 p-6 shadow-soft backdrop-blur-sm">
        <p className="font-display text-xs uppercase tracking-[0.2em] text-slateBlue-700">
          Smart Developer Growth Platform
        </p>
        <div className="mt-3 flex flex-wrap items-end justify-between gap-3">
          <div>
            <h1 className="font-display text-2xl text-slateBlue-900 sm:text-3xl">
              Developer Performance Dashboard
            </h1>
            <p className="mt-1 text-sm text-slate-600">
              Logged in as <span className="font-semibold">{user?.name}</span> ({user?.email})
            </p>
          </div>
          <div className="flex flex-wrap items-center gap-2">
            <button
              type="button"
              onClick={handleLogout}
              className="rounded-xl border border-slateBlue-200 bg-white px-4 py-2 text-sm font-semibold text-slateBlue-900 transition hover:bg-slateBlue-50"
            >
              Logout
            </button>
          </div>
        </div>
      </header>

      <section className="grid gap-6 lg:grid-cols-[minmax(280px,360px)_1fr]">
        <div className="space-y-6">
          <div className="rise rounded-3xl bg-white p-6 shadow-soft" style={{ animationDelay: "70ms" }}>
            <h2 className="font-display text-xl text-slateBlue-900">Actions</h2>
            <p className="mt-1 text-sm text-slate-500">
              Generate new tasks or refresh your latest progress snapshot.
            </p>

            <div className="mt-4 grid grid-cols-2 gap-3">
              <button
                type="button"
                onClick={refreshDashboard}
                className="rounded-xl border border-slateBlue-200 bg-white px-3 py-2 text-sm font-semibold text-slateBlue-900 transition hover:bg-slateBlue-50"
              >
                Refresh
              </button>
              <button
                type="button"
                onClick={handleGenerateTasks}
                disabled={isGeneratingTasks}
                className="rounded-xl bg-mint-500 px-3 py-2 text-sm font-semibold text-white transition hover:brightness-95 disabled:cursor-not-allowed disabled:opacity-60"
              >
                {isGeneratingTasks ? "Generating..." : "Generate Tasks"}
              </button>
            </div>
          </div>

          <div className="rise rounded-3xl bg-white p-5 shadow-soft" style={{ animationDelay: "130ms" }}>
            <h3 className="font-display text-lg text-slateBlue-900">Run Status</h3>
            <p className="mt-2 text-sm text-slate-500">
              {isRefreshing || isPending ? "Refreshing dashboard..." : "Dashboard is ready."}
            </p>
            {statusMessage && (
              <p className="mt-3 rounded-xl bg-mint-100 px-3 py-2 text-sm font-semibold text-emerald-800">
                {statusMessage}
              </p>
            )}
            {errorMessage && (
              <p className="mt-3 rounded-xl bg-red-50 px-3 py-2 text-sm font-semibold text-red-700">
                {errorMessage}
              </p>
            )}
          </div>
        </div>

        <div className="space-y-6">
          <div className="rise grid gap-4 sm:grid-cols-3" style={{ animationDelay: "90ms" }}>
            <MetricCard label="Total Tasks" value={progress?.totalTasks ?? 0} tone="slate" />
            <MetricCard label="Completed" value={progress?.completedTasks ?? 0} tone="mint" />
            <MetricCard
              label="Completion %"
              value={`${progress?.completionPercentage ?? 0}%`}
              tone="amber"
            />
          </div>

          <div className="rise rounded-3xl bg-white p-6 shadow-soft" style={{ animationDelay: "120ms" }}>
            <div className="flex items-center justify-between">
              <h2 className="font-display text-xl text-slateBlue-900">Task List</h2>
              <span className="rounded-full bg-slateBlue-50 px-3 py-1 text-xs font-semibold text-slateBlue-700">
                {tasks.length} tasks
              </span>
            </div>

            {tasks.length === 0 ? (
              <p className="mt-4 text-sm text-slate-500">No tasks yet. Generate tasks to start tracking growth.</p>
            ) : (
              <ul className="mt-4 space-y-3">
                {tasks.map((task) => (
                  <li key={task.id} className="rounded-2xl border border-slateBlue-100 p-4">
                    <div className="flex flex-wrap items-center justify-between gap-2">
                      <p className="font-semibold text-slateBlue-900">{task.title}</p>
                      <span
                        className={`rounded-full px-2.5 py-1 text-xs font-bold ${
                          task.status === "DONE"
                            ? "bg-mint-100 text-emerald-800"
                            : "bg-amberSoft-100 text-amber-800"
                        }`}
                      >
                        {task.status === "DONE" ? "✅ DONE" : "PENDING"}
                      </span>
                    </div>
                    <div className="mt-2 flex flex-wrap gap-2">
                      <Tag>{task.category}</Tag>
                      <Tag>{task.createdAt?.slice(0, 10)}</Tag>
                    </div>
                    {task.status !== "DONE" && (
                      <button
                        type="button"
                        onClick={() => handleMarkTaskAsDone(task.id)}
                        disabled={completingTaskId === task.id}
                        className="mt-3 rounded-xl bg-slateBlue-700 px-3 py-2 text-xs font-semibold text-white transition hover:bg-slateBlue-900 disabled:cursor-not-allowed disabled:opacity-60"
                      >
                        {completingTaskId === task.id ? "Marking..." : "Mark as Done"}
                      </button>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div className="grid gap-6 xl:grid-cols-2">
            <div className="rise rounded-3xl bg-white p-6 shadow-soft" style={{ animationDelay: "150ms" }}>
              <h2 className="font-display text-xl text-slateBlue-900">Weakness Analysis</h2>
              {weaknessEntries.length === 0 ? (
                <p className="mt-3 text-sm text-slate-500">No weakness data available for this user yet.</p>
              ) : (
                <ul className="mt-4 space-y-3">
                  {weaknessEntries.map(([category, details]) => (
                    <li key={category} className="rounded-2xl border border-slateBlue-100 p-3">
                      <div className="flex items-center justify-between gap-2">
                        <p className="font-semibold text-slateBlue-900">{category}</p>
                        <span
                          className={`rounded-full px-2.5 py-1 text-xs font-bold ${
                            details.status === "WEAK"
                              ? "bg-red-50 text-red-700"
                              : "bg-mint-100 text-emerald-800"
                          }`}
                        >
                          {details.status}
                        </span>
                      </div>
                      <p className="mt-2 text-sm text-slate-600">{details.suggestion}</p>
                    </li>
                  ))}
                </ul>
              )}
            </div>

            <div className="rise rounded-3xl bg-white p-6 shadow-soft" style={{ animationDelay: "180ms" }}>
              <h2 className="font-display text-xl text-slateBlue-900">AI Insight</h2>
              {insight ? (
                <div className="mt-4 space-y-3">
                  <p className="rounded-2xl bg-slateBlue-50 p-4 text-sm leading-6 text-slateBlue-900">
                    {insight.insight}
                  </p>
                  <p className="text-sm text-slate-600">
                    Completion: <span className="font-bold">{insight.completionPercentage}%</span>
                  </p>
                  <div className="flex flex-wrap gap-2">
                    {insight.weakAreas.length === 0 ? (
                      <Tag>No weak areas</Tag>
                    ) : (
                      insight.weakAreas.map((area) => <Tag key={area}>{area}</Tag>)
                    )}
                  </div>
                </div>
              ) : (
                <p className="mt-3 text-sm text-slate-500">Insight will appear after dashboard data is loaded.</p>
              )}
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}

function MetricCard({ label, value, tone }) {
  const palette =
    tone === "mint"
      ? "from-mint-100 to-white text-emerald-900"
      : tone === "amber"
        ? "from-amberSoft-100 to-white text-amber-900"
        : "from-slateBlue-100 to-white text-slateBlue-900";

  return (
    <div className={`rounded-3xl bg-gradient-to-br ${palette} p-5 shadow-soft`}>
      <p className="text-xs font-semibold uppercase tracking-[0.15em]">{label}</p>
      <p className="mt-3 font-display text-3xl">{value}</p>
    </div>
  );
}

function Tag({ children }) {
  return (
    <span className="rounded-full border border-slateBlue-200 bg-white px-2.5 py-1 text-xs font-semibold text-slateBlue-700">
      {children}
    </span>
  );
}

export default Dashboard;
