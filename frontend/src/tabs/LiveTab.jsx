import { useTradingBot } from '../hooks/useTradingBot';
import TradesTable from '../components/TradesTable';
import PortfolioChart from '../components/PortfolioChart';
import { reconstructBackwards } from '../utils/derivePortfolioHistory';

export default function LiveTab() {
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
    } = tradesPager;

    const portfolioHistory =
        portfolio && trades.length > 0
            ? reconstructBackwards(
                trades,
                portfolio.cash,
                portfolio.assetQuantity
            )
            : [];

    if (loading) return <p>Loading…</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <>
            <p>
                Status:{' '}
                <strong style={{ color: running ? 'green' : 'red' }}>
                    {running ? 'RUNNING' : 'STOPPED'}
                </strong>
            </p>

            <div className="portfolio">
                <div>Cash: {portfolio.cash}</div>
                <div>Asset qty: {portfolio.assetQuantity}</div>
                <div>Total value: {portfolio.totalValue}</div>
            </div>

            <button onClick={start} disabled={running}>
                Start
            </button>

            <button onClick={stop} disabled={!running} style={{marginLeft: 8 }}>
                Stop
            </button>

            <button
                onClick={() => reset(10000)}
                style={{ marginLeft: 8, background: '#f87171' }}
            >
                Reset
            </button>

            {portfolioHistory.length > 0 && (
                <PortfolioChart data={portfolioHistory} />
            )}

            <div style={{ marginTop: 16 }}>
                <button
                    onClick={() => setCurrentPage(p => p - 1)}
                    disabled={!hasPrevPage}
                >
                    Prev
                </button>

                <span style={{ margin: '0 12px' }}>
                    Page {currentPage + 1}
                </span>

                <button
                    onClick={loadNextPage}
                    disabled={!hasNextPage}
                >
                    Next
                </button>
            </div>

            <TradesTable trades={trades} />
        </>
    );
}