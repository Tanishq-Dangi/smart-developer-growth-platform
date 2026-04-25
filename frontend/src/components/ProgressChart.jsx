import {
    PieChart,
    Pie,
    Cell,
    Tooltip,
    Legend,
    ResponsiveContainer
} from "recharts";

const COLORS = ["#22c55e", "#facc15"];

export default function ProgressChart({ completed, pending }) {
    const data = [
        { name: "Completed", value: completed },
        { name: "Pending", value: pending }
    ];

    return (
        <div className="bg-white rounded-2xl p-5 shadow-md">
            <h3 className="text-lg font-semibold mb-3">Progress Overview</h3>

            <ResponsiveContainer width="100%" height={250}>
                <PieChart>
                    <Pie
                        data={data}
                        cx="50%"
                        cy="50%"
                        outerRadius={80}
                        dataKey="value"
                        label={({ percent }) =>
                            `${(percent * 100).toFixed(0)}%`
                        }
                    >
                        {data.map((entry, index) => (
                            <Cell key={index} fill={COLORS[index]} />
                        ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
}