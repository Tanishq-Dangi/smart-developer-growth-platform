import { useEffect, useState } from "react";
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

import ProgressChart from "../components/ProgressChart";
import WeeklyProgressChart from "../components/WeeklyProgressChart";

function Dashboard() {
  const navigate = useNavigate();
  const [user] = useState(() => getStoredUser());

  const [tasks, setTasks] = useState([]);
  const [progress, setProgress] = useState(null);
  const [weakness, setWeakness] = useState({});
  const [insight, setInsight] = useState(null);

  async function refreshDashboard() {
    try {
      const [tasksData, progressData, weaknessData, insightData] =
          await Promise.all([
            getTasks(),
            getProgress(),
            getWeakness(),
            getInsight()
          ]);

      setTasks(tasksData);
      setProgress(progressData);
      setWeakness(weaknessData);
      setInsight(insightData);
    } catch (e) {
      console.error(e);
    }
  }

  useEffect(() => {
    if (!user?.email) {
      clearAuthData();
      navigate("/login");
      return;
    }
    refreshDashboard();
  }, []);

  async function handleGenerateTasks() {
    await generateTasks();
    refreshDashboard();
  }

  async function handleMarkTaskAsDone(id) {
    await completeTask(id);
    refreshDashboard();
  }

  function handleLogout() {
    clearAuthData();
    navigate("/login");
  }

  const totalTasks = progress?.totalTasks ?? 0;
  const completedTasks = progress?.completedTasks ?? 0;
  const completionPercent = progress?.completionPercentage ?? 0;

  // 🔥 Weekly Dummy Data (later backend se aayega)
  const weeklyData = [
    { day: "Mon", progress: 20 },
    { day: "Tue", progress: 40 },
    { day: "Wed", progress: 60 },
    { day: "Thu", progress: 70 },
    { day: "Fri", progress: 80 },
    { day: "Sat", progress: 90 },
    { day: "Sun", progress: completionPercent },
  ];

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
          <button onClick={refreshDashboard} className="rounded bg-gray-200 px-4 py-2">
            Refresh
          </button>

          <button onClick={handleGenerateTasks} className="rounded bg-green-500 px-4 py-2 text-white">
            Generate Tasks
          </button>
        </div>

        {/* METRICS */}
        <div className="grid grid-cols-3 gap-4 mb-6">
          <MetricCard label="Total Tasks" value={totalTasks} />
          <MetricCard label="Completed" value={completedTasks} />
          <MetricCard label="Completion %" value={`${completionPercent}%`} />
        </div>

        {/* 🔥 PROGRESS OVERVIEW (FIXED) */}
        <div className="bg-white p-6 rounded-2xl shadow mb-6">
          <h2 className="text-lg font-semibold mb-4">Progress Overview</h2>

          <div className="flex flex-col md:flex-row items-center justify-between gap-6">

            {/* LEFT PIE */}
            <ProgressChart
                completed={completedTasks}
                total={totalTasks}
            />

            {/* RIGHT WEEKLY GRAPH */}
            <WeeklyProgressChart data={weeklyData} />

          </div>
        </div>

        {/* TASK LIST */}
        <div className="mb-6 rounded-2xl bg-white p-5 shadow">
          <h2 className="text-lg font-semibold mb-3">Task List</h2>

          {tasks.map(task => (
              <div key={task.id} className="mb-3 flex justify-between border p-3 rounded">
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
          ))}
        </div>

        {/* WEAKNESS */}
        <div className="mb-6 rounded-2xl bg-white p-5 shadow">
          <h2 className="text-lg font-semibold">Weakness</h2>

          {Object.entries(weakness).map(([key, val]) => (
              <p key={key}>{key}: {val.status}</p>
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