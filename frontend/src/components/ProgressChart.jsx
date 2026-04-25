import { PieChart, Pie, Cell, Tooltip, Legend } from "recharts";

const COLORS = ["#22c55e", "#eab308"];

export default function ProgressChart({ completed, total }) {
    const pending = total - completed;

    const data = [
        { name: "Completed", value: completed },
        { name: "Pending", value: pending },
    ];

    return (
        <PieChart width={300} height={250}>
            <Pie
                data={data}
                cx="50%"
                cy="50%"
                outerRadius={80}
                dataKey="value"
                labelLine={false}
                label={({ percent }) => `${(percent * 100).toFixed(0)}%`}
            >
                {data.map((entry, index) => (
                    <Cell key={index} fill={COLORS[index]} />
                ))}
            </Pie>
            <Tooltip />
            <Legend />
        </PieChart>
    );
}