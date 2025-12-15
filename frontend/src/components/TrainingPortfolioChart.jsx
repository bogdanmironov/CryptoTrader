import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
} from 'recharts';

export default function TrainingPortfolioChart({ data }) {
    if (!data || data.length === 0) {
        return <p>No portfolio data yet.</p>;
    }

    return (
        <div style={{ height: 300, marginTop: 24 }}>
            <ResponsiveContainer width="100%" height="100%">
                <LineChart data={data}>
                    <XAxis
                        dataKey="time"
                        tickFormatter={t => new Date(t).toLocaleTimeString()}
                    />
                    <YAxis />
                    <Tooltip
                        labelFormatter={t => new Date(t).toLocaleTimeString()}
                    />
                    <Line
                        dataKey="value"
                        stroke="#22c55e"
                        dot={false}
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
}