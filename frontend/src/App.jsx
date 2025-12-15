import { useTradingBot } from './hooks/useTradingBot';
import TradesTable from './components/TradesTable';

function App() {
    const {
        running,
        portfolio,
        trades,
        hasMore,
        loading,
        error,
        start,
        stop,
        loadMoreTrades
    } = useTradingBot();

    if (loading) return <p>Loading…</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <div style={{ padding: 24 }}>
            <h1>Crypto Trading Bot</h1>

            <p>
                Status:{' '}
                <strong style={{ color: running ? 'green' : 'red' }}>
                    {running ? 'RUNNING' : 'STOPPED'}
                </strong>
            </p>

            <p>Portfolio value: {portfolio}</p>

            <button onClick={start} disabled={running}>
                Start
            </button>

            <button onClick={stop} disabled={!running} style={{ marginLeft: 8 }}>
                Stop
            </button>

            <TradesTable trades={trades} />

            {hasMore && (
                <button
                    onClick={loadMoreTrades}
                    style={{ marginTop: 16 }}
                >
                    Load more
                </button>
            )}
        </div>
    );
}

export default App;