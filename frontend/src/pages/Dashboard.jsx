import { useEffect, useState, useTransition } from "react";
import { useNavigate } from "react-router-dom";
import {
  completeTask,
  generateTasks,
  getInsight,
  getProgress,
  getTasks,
  getWeakness
} from "../api/platformApi";
import { clearAuthData, getStoredUser } from "../auth/storage";
import ProgressChart from "../components/ProgressChart"; // ✅ ADDED

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
    if (!user?.email) return;

    setErrorMessage("");
    setIsRefreshing(true);

    try {
      const [tasksData, progressData, weaknessData, insightData] =
          await Promise.all([
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
    if (!user?.email) return;

    setErrorMessage("");
    setStatusMessage("");
    setIsGeneratingTasks(true);

    try {
      const generatedTasks = await generateTasks();

      if (generatedTasks.length === 0) {
        setStatusMessage(
            "No new tasks generated. Today's recommendations already exist."
        );
      } else {
        setStatusMessage(`${generatedTasks.length} tasks generated.`);
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
      await completeTask(taskId);
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
      <div className="mx-auto max-w-7xl px-4 pb-10 pt-8">
        {/* HEADER */}
        <header className="mb-6 rounded-2xl bg-white p-5 shadow">
          <h1 className="text-2xl font-bold">Developer Performance Dashboard</h1>
          <p className="text-sm text-gray-600">
            {user?.name} ({user?.email})
          </p>

          <button
              onClick={handleLogout}
              className="mt-2 rounded bg-red-500 px-3 py-1 text-white"
          >
            Logout
          </button>
        </header>

        {/* ACTIONS */}
        <div className="mb-6 flex gap-3">
          <button
              onClick={refreshDashboard}
              className="rounded bg-gray-200 px-4 py-2"
          >
            Refresh
          </button>

          <button
              onClick={handleGenerateTasks}
              className="rounded bg-green-500 px-4 py-2 text-white"
          >
            Generate Tasks
          </button>
        </div>

        {/* METRICS */}
        <div className="grid grid-cols-3 gap-4 mb-6">
          <MetricCard label="Total Tasks" value={progress?.totalTasks ?? 0} />
          <MetricCard
              label="Completed"
              value={progress?.completedTasks ?? 0}
          />
          <MetricCard
              label="Completion %"
              value={`${progress?.completionPercentage ?? 0}%`}
          />
        </div>

        {/* 🔥 PROGRESS CHART */}
        <div className="mb-6">
          <ProgressChart
              completed={progress?.completedTasks ?? 0}
              pending={
                  (progress?.totalTasks ?? 0) -
                  (progress?.completedTasks ?? 0)
              }
          />
        </div>

        {/* TASK LIST */}
        <div className="mb-6 rounded-2xl bg-white p-5 shadow">
          <h2 className="text-lg font-semibold mb-3">Task List</h2>

          {tasks.length === 0 ? (
              <p>No tasks</p>
          ) : (
              tasks.map((task) => (
                  <div
                      key={task.id}
                      className="mb-3 rounded border p-3 flex justify-between"
                  >
                    <div>
                      <p className="font-semibold">{task.title}</p>
                      <p className="text-sm text-gray-500">{task.category}</p>
                    </div>

                    {task.status === "DONE" ? (
                        <span className="text-green-600 font-bold">DONE</span>
                    ) : (
                        <button
                            onClick={() => handleMarkTaskAsDone(task.id)}
                            className="bg-blue-500 text-white px-3 py-1 rounded"
                        >
                          Mark Done
                        </button>
                    )}
                  </div>
              ))
          )}
        </div>

        {/* WEAKNESS */}
        <div className="mb-6 rounded-2xl bg-white p-5 shadow">
          <h2 className="text-lg font-semibold">Weakness</h2>

          {weaknessEntries.map(([key, val]) => (
              <p key={key}>
                {key}: {val.status}
              </p>
          ))}
        </div>

        {/* INSIGHT */}
        <div className="rounded-2xl bg-white p-5 shadow">
          <h2 className="text-lg font-semibold">AI Insight</h2>
          <p>{insight?.insight}</p>
        </div>
      </div>
  );
}

function MetricCard({ label, value }) {
  return (
      <div className="rounded-xl bg-white p-4 shadow text-center">
        <p className="text-sm text-gray-500">{label}</p>
        <p className="text-xl font-bold">{value}</p>
      </div>
  );
}

export default Dashboard;