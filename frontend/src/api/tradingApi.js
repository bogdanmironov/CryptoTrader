import { apiFetch } from './client';

export function getTradingStatus() {
    return apiFetch('/trades/status');
}

export function startTrading() {
    return apiFetch('/trades/start', {
        method: 'POST',
    });
}

export function stopTrading() {
    return apiFetch('/trades/stop', {
        method: 'POST',
    });
}

export function resetTrading() {
    return apiFetch('/trades/reset', { method: 'POST' });
}

export function getTrades({ limit = 20, cursor = null } = {}) {
    const params = new URLSearchParams({ limit });

    if (cursor) {
        params.append('cursor', cursor);
    }

    return apiFetch(`/trades?${params.toString()}`);
}


export function getPortfolioValue() {
    return apiFetch('/portfolio');
}