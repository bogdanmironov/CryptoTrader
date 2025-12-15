import { apiFetch } from './client.js';

export function runTraining({ symbol, from, to, initialBalance }) {
    return apiFetch('/training/run', {
        method: 'POST',
        body: JSON.stringify({
            symbol,
            from,
            to,
            initialBalance,
        }),
    });
}