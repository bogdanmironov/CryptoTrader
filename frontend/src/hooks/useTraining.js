import { useState, useCallback } from 'react';
import { runTraining } from '../api/trainingApi';

export function useTraining() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [result, setResult] = useState(null);

    const run = useCallback(async (payload) => {
        try {
            setLoading(true);
            setError(null);
            const res = await runTraining(payload);
            setResult(res);
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }, []);

    return {
        loading,
        error,
        result,
        run,
    };
}