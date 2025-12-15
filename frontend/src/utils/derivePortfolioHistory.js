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

export function reconstructBackwards(
    trades,
    currentCash,
    currentAsset
) {
    let cash = Number(currentCash)
    let asset = Number(currentAsset)

    const timeline = []

    for (let i = trades.length - 1; i >= 0; i--) {
        const t = trades[i]

        timeline.push({
            time: t.executedAt,
            value: cash + asset * t.quotePrice,
            cash,
            asset,
        })

        if (t.action === 'BUY') {
            asset -= t.quantity
            cash += t.quantity * t.quotePrice
        } else if (t.action === 'SELL') {
            asset += t.quantity
            cash -= t.quantity * t.quotePrice
        }
    }

    return timeline.reverse()
}