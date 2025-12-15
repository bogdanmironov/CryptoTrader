function TradesTable({ trades }) {
    if (!trades || trades.length === 0) {
        return <p>No trades yet.</p>;
    }

    return (
        <div style={{ marginTop: 24 }}>
            <h2>Recent Trades</h2>

            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                <tr>
                    <th>Time</th>
                    <th>Action</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Δ Balance</th>
                </tr>
                </thead>
                <tbody>
                {trades.map(trade => (
                    <tr key={trade.id}>
                        <td>{new Date(trade.executedAt).toLocaleTimeString()}</td>
                        <td style={{ color: trade.action === 'BUY' ? 'green' : 'red' }}>
                            {trade.action}
                        </td>
                        <td>{trade.quantity.toFixed(6)}</td>
                        <td>{trade.quotePrice.toFixed(2)}</td>
                        <td style={{ color: trade.transactionDelta < 0 ? 'red' : 'green' }}>
                            {trade.transactionDelta.toFixed(2)}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default TradesTable;