import { apiFetch } from "./client";

export async function getTrades(limit = 20, cursor = null) {
    const params = new URLSearchParams();

    if (limit != null) {
        params.set('limit', limit);
    }

    if (cursor != null) {
        params.set('cursor', cursor);
    }

    return apiFetch(`/trades?${params.toString()}`);
}