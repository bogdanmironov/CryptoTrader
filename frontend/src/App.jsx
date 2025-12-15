import { useTradingBot } from './hooks/useTradingBot';
import TradesTable from './components/TradesTable';
import PortfolioChart from './components/PortfolioChart';
import { derivePortfolioHistory } from './utils/derivePortfolioHistory';

function App() {
    const {
        running,
        portfolio,
        loading,
        error,
        start,
        stop,
        reset,
        tradesPager,
    } = useTradingBot();

    const {
        trades,
        currentPage,
        hasPrevPage,
        hasNextPage,
        setCurrentPage,
        loadNextPage,
        loadFirstPage,
    } = tradesPager;

    const portfolioHistory = derivePortfolioHistory(trades);

    if (loading) return <p>Loading…</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <div style={{padding: 24}}>
            <h1>Crypto Trading Bot</h1>

            <p>
                Status:{' '}
                <strong style={{color: running ? 'green' : 'red'}}>
                    {running ? 'RUNNING' : 'STOPPED'}
                </strong>
            </p>

            <p>Portfolio value: {portfolio}</p>

            <button onClick={start} disabled={running}>
                Start
            </button>

            <button onClick={stop} disabled={!running} style={{marginLeft: 8}}>
                Stop
            </button>

            <button
                onClick={() => reset(10000)}
                style={{marginLeft: 8, background: '#f87171'}}
            >
                Reset
            </button>

            <PortfolioChart data={portfolioHistory}/>

            <div style={{marginTop: 16}}>
                <button
                    onClick={() => setCurrentPage(p => p - 1)}
                    disabled={!hasPrevPage}
                >
                    Prev
                </button>

                <span style={{margin: '0 12px'}}>
                    Page {currentPage + 1}
                </span>

                <button
                    onClick={loadNextPage}
                    disabled={!hasNextPage}
                >
                    Next
                </button>
            </div>

            <TradesTable trades={trades}/>
        </div>
    );
}

export default App;