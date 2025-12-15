import { useEffect, useState } from 'react';
import {
    getTradingStatus,
    startTrading,
    stopTrading
} from '../api/tradingApi';
import { getPortfolio } from '../api/portfolioApi';
import { getTrades } from '../api/tradesHistoryApi';

export function useTradingBot() {
    const [running, setRunning] = useState(false);
    const [portfolio, setPortfolio] = useState(null);

    const [trades, setTrades] = useState([]);
    const [cursor, setCursor] = useState(null);
    const [hasMore, setHasMore] = useState(true);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    async function refreshStatus() {
        const res = await getTradingStatus();
        setRunning(res.running);
    }

    async function refreshPortfolio() {
        const value = await getPortfolio();
        setPortfolio(value);
    }

    async function loadInitialTrades() {
        const res = await getTrades(20, null);
        setTrades(res.trades);
        setCursor(res.nextCursor);
        setHasMore(res.nextCursor !== null);
    }

    async function loadMoreTrades() {
        if (!hasMore) return;

        const res = await getTrades(20, cursor);

        setTrades(prev => [...prev, ...res.trades]);
        setCursor(res.nextCursor);
        setHasMore(res.nextCursor !== null);
    }

    async function start() {
        await startTrading();
        await refreshStatus();
    }

    async function stop() {
        await stopTrading();
        await refreshStatus();
    }

    useEffect(() => {
        async function init() {
            try {
                setLoading(true);
                await Promise.all([
                    refreshStatus(),
                    refreshPortfolio(),
                    loadInitialTrades()
                ]);
            } catch (e) {
                setError(e.message);
            } finally {
                setLoading(false);
            }
        }

        init();
    }, []);

    useEffect(() => {
        if (!running) return;

        const id = setInterval(() => {
            refreshPortfolio();
        }, 3000);

        return () => clearInterval(id);
    }, [running]);

    return {
        running,
        portfolio,
        trades,
        hasMore,
        loading,
        error,
        start,
        stop,
        loadMoreTrades
    };
}