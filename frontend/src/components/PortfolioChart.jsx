import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer
} from 'recharts';

export default function PortfolioChart({ data }) {
    if (!data || data.length === 0) {
        return <p>No portfolio data yet.</p>;
    }

    return (
        <div style={{ marginTop: 32 }}>
            <h2>Portfolio value over time</h2>

            <ResponsiveContainer width="100%" height={300}>
                <LineChart data={data}>
                    <XAxis
                        dataKey="time"
                        tickFormatter={t =>
                            new Date(t).toLocaleTimeString()
                        }
                    />
                    <YAxis domain={['auto', 'auto']} />
                    <Tooltip
                        labelFormatter={t =>
                            new Date(t).toLocaleTimeString()
                        }
                    />
                    <Line
                        type="monotone"
                        dataKey="value"
                        stroke="#2563eb"
                        dot={false}
                    />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
}