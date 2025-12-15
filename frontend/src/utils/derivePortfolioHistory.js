export function derivePortfolioHistory(trades) {
    if (!trades || trades.length === 0) return [];

    const sorted = [...trades].sort(
        (a, b) => new Date(a.executedAt) - new Date(b.executedAt)
    );

    let cumulativePnL = 0;

    return sorted.map(trade => {
        cumulativePnL += trade.transactionDelta;

        return {
            time: trade.executedAt,
            value: cumulativePnL,
        };
    });
}