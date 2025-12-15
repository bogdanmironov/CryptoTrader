export default function TradesTable({ trades }) {
    if (!trades || trades.length === 0) return <p>No trades yet.</p>;

    return (
        <div style={{ marginTop: 24 }}>
            <h2>Trades</h2>

            <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: 8 }}>
                <thead>
                <tr>
                    <th align="left">Time</th>
                    <th align="left">Action</th>
                    <th align="right">Quantity</th>
                    <th align="right">Price</th>
                    <th align="right">Δ Balance</th>
                </tr>
                </thead>

                <tbody>
                {trades.map(trade => (
                    <tr key={trade.id}>
                        <td>{new Date(trade.executedAt).toLocaleString()}</td>

                        <td
                            style={{
                                color: trade.action === 'BUY' ? 'green' : 'red',
                                fontWeight: 'bold',
                            }}
                        >
                            {trade.action}
                        </td>

                        <td align="right">{Number(trade.quantity).toFixed(6)}</td>
                        <td align="right">{Number(trade.quotePrice).toFixed(2)}</td>

                        <td
                            align="right"
                            style={{ color: trade.transactionDelta < 0 ? 'red' : 'green' }}
                        >
                            {Number(trade.transactionDelta).toFixed(2)}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}