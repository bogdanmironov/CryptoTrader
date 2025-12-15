import { useCallback, useEffect, useState } from 'react';
import { getTradingStatus, startTrading, stopTrading } from '../api/tradingApi';
import { getPortfolio } from '../api/portfolioApi';
import { useTradesPagination } from './useTradesPagination';

export function useTradingBot() {
    const [running, setRunning] = useState(false);
    const [portfolio, setPortfolio] = useState(null);
    const [portfolioHistory, setPortfolioHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const tradesPager = useTradesPagination({
        running,
        pageSize: 20,
        pollMs: 3000,
    });

    const refreshStatus = useCallback(async () => {
        const res = await getTradingStatus();
        setRunning(!!res.running);
    }, []);

    const refreshPortfolio = useCallback(async () => {
        try {
            const value = await getPortfolio();
            setPortfolio(value);

            setPortfolioHistory(prev => [
                ...prev,
                {
                    time: new Date().toISOString(),
                    value
                }
            ]);
        } catch (e) {
            setError(e.message);
        }
    }, []);

    const start = useCallback(async () => {
        await startTrading();
        await refreshStatus();
        await tradesPager.loadFirstPage();
    }, [refreshStatus, tradesPager]);

    const stop = useCallback(async () => {
        await stopTrading();
        await refreshStatus();
    }, [refreshStatus]);

    useEffect(() => {
        async function init() {
            try {
                setLoading(true);
                setPortfolioHistory([]);
                await Promise.all([
                    refreshStatus(),
                    refreshPortfolio(),
                    tradesPager.loadFirstPage(),
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

        const id = setInterval(refreshPortfolio, 3000);
        return () => clearInterval(id);
    }, [running, refreshPortfolio]);

    return {
        running,
        portfolio,
        portfolioHistory,
        loading,
        error,
        start,
        stop,
        tradesPager,
    };
}