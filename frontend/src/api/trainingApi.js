import { apiFetch } from './client';

export async function runTraining(payload) {
    return apiFetch('/training/run', {
        method: 'POST',
        body: JSON.stringify(payload),
    });
}