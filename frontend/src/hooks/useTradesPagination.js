import { useCallback, useEffect, useRef, useState } from 'react';
import { getTrades } from '../api/tradesHistoryApi';

export function useTradesPagination({
                                        running,
                                        pageSize = 20,
                                        pollMs = 3000,
                                    }) {
    const [pages, setPages] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const cursorsRef = useRef([null]);
    const pollRef = useRef(null);


    const loadFirstPage = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            const res = await getTrades(pageSize, null);

            setPages([res.trades]);
            cursorsRef.current = [null, res.nextCursor];
            setCurrentPage(0);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }, [pageSize]);

    const loadNextPage = useCallback(async () => {
        const cursor = cursorsRef.current[currentPage + 1];
        if (!cursor) return;

        try {
            setLoading(true);
            setError(null);

            const res = await getTrades(pageSize, cursor);

            setPages(prev => [...prev, res.trades]);
            cursorsRef.current[currentPage + 2] = res.nextCursor;
            setCurrentPage(p => p + 1);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }, [currentPage, pageSize]);

    const reset = useCallback(() => {
        if (pollRef.current) {
            clearInterval(pollRef.current);
            pollRef.current = null;
        }

        setPages([]);
        setCurrentPage(0);
        setLoading(false);
        setError(null);

        cursorsRef.current = [null];
    }, []);

    useEffect(() => {
        if (!running) return;

        pollRef.current = setInterval(async () => {
            try {
                const res = await getTrades(pageSize, null);
                if (pages.length === 0) return;

                const existingIds = new Set(
                    pages[0].map(t => t.id)
                );

                const newTrades = res.trades.filter(
                    t => !existingIds.has(t.id)
                );

                if (newTrades.length === 0) return;

                setPages(prev => {
                    const merged = [...newTrades, ...prev[0]];

                    const nextPages = [...prev];
                    nextPages[0] = merged.slice(0, pageSize);

                    // overflow pushes into next pages
                    let overflow = merged.slice(pageSize);
                    let i = 1;

                    while (overflow.length > 0) {
                        const next = nextPages[i] ?? [];
                        const combined = [...overflow, ...next];
                        nextPages[i] = combined.slice(0, pageSize);
                        overflow = combined.slice(pageSize);
                        i++;
                    }

                    return nextPages;
                });
            } catch {
                // polling errors ignored
            }
        }, pollMs);

        return () => clearInterval(pollRef.current);
    }, [running, pages, pageSize, pollMs]);

    return {
        pages,
        trades: pages[currentPage] ?? [],
        currentPage,

        hasPrevPage: currentPage > 0,
        hasNextPage: !!cursorsRef.current[currentPage + 1],

        setCurrentPage,
        loadFirstPage,
        loadNextPage,

        reset,
        loading,
        error,
    };
}