import { useState } from 'react';
import { useTraining } from '../hooks/useTraining';
import TrainingPortfolioChart from '../components/TrainingPortfolioChart';
import TrainingTradesTable from '../components/TrainingTradesTable';

function toInstant(value) {
    if (!value) return null;
    return value.length === 16
        ? value + ':00Z'
        : value.endsWith('Z')
            ? value
            : value + 'Z';
}

function fromInstant(value) {
    if (!value) return '';
    return value.replace('Z', '').slice(0, 16);
}

export default function TrainingTab() {
    const { run, result, loading, error } = useTraining();

    const [form, setForm] = useState({
        symbol: 'BTCUSDT',
        from: '2024-12-01T00:00:00Z',
        to: '2024-12-30T12:00:00Z',
        initialBalance: 10000,
    });

    return (
        <div style={{ marginTop: 24 }}>
            <h2>Training mode</h2>

            {/* --- FORM --- */}
            <div style={{ maxWidth: 420, display: 'grid', gap: 12 }}>
                <label>
                    Symbol
                    <input
                        type="text"
                        value={form.symbol}
                        onChange={e =>
                            setForm({ ...form, symbol: e.target.value })
                        }
                    />
                </label>

                <label>
                    From
                    <input
                        type="datetime-local"
                        value={fromInstant(form.from)}
                        onChange={e =>
                            setForm({
                                ...form,
                                from: toInstant(e.target.value),
                            })
                        }
                    />
                </label>

                <label>
                    To
                    <input
                        type="datetime-local"
                        value={fromInstant(form.to)}
                        onChange={e =>
                            setForm({
                                ...form,
                                to: toInstant(e.target.value),
                            })
                        }
                    />
                </label>

                <label>
                    Initial balance
                    <input
                        type="number"
                        min={1}
                        step={100}
                        value={form.initialBalance}
                        onChange={e =>
                            setForm({
                                ...form,
                                initialBalance: Number(e.target.value),
                            })
                        }
                    />
                </label>

                <button
                    onClick={() => run(form)}
                    disabled={loading}
                    style={{ marginTop: 8 }}
                >
                    {loading ? 'Running…' : 'Run training'}
                </button>

                {error && (
                    <p style={{ color: 'red' }}>
                        {typeof error === 'string' ? error : JSON.stringify(error)}
                    </p>
                )}
            </div>

            {/* --- RESULT --- */}
            {result && (
                <div style={{ marginTop: 32 }}>
                    <h3>Training result</h3>

                    <p>
                        Initial: <strong>{result.initialBalance}</strong> → Final:{' '}
                        <strong>{result.finalBalance}</strong>
                    </p>

                    <TrainingPortfolioChart data={result.portfolioTimeline} />

                    <TrainingTradesTable trades={result.trades} />
                </div>
            )}
        </div>
    );
}