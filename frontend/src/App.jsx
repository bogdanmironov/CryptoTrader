import { useState } from 'react';
import LiveTab from './tabs/LiveTab';
import TrainingTab from './tabs/TrainingTab';

function App() {
    const [tab, setTab] = useState('LIVE');

    return (
        <div style={{ padding: 24 }}>
            <h1>Crypto Trading Bot</h1>

            <div style={{ marginBottom: 16 }}>
                <button
                    onClick={() => setTab('LIVE')}
                    disabled={tab === 'LIVE'}
                >
                    Live
                </button>

                <button
                    onClick={() => setTab('TRAIN')}
                    disabled={tab === 'TRAIN'}
                    style={{ marginLeft: 8 }}
                >
                    Training
                </button>
            </div>

            {tab === 'LIVE' && <LiveTab />}
            {tab === 'TRAIN' && <TrainingTab />}
        </div>
    );
}

export default App;