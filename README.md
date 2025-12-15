# CryptoTrader

### Web application that simulates an automated crypto trading bot


### Short demo:
https://drive.google.com/drive/folders/1paMdYQLaFkpFdIM_gWF1PA-t3wISUYsI?usp=drive_link

## Solution overview & design decisions

The system is split into a **Spring Boot backend** and a **React frontend**.

### Backend
- Fetching market price data from a public crypto API
- Executing trading logic
- Persisting trades, account state and assets in a relational database

The trading bot operates in two modes:
- **Training mode** – backtesting on historical data
- **Live trading mode** – simulated trading on live price ticks

### Frontend
- Live and training mode dashboards
- Trade history table
- Portfolio value visualization over time
- Controls to start, stop, reset and switch modes

---

## Trading logic & reasoning

The trading strategy is based on a **Simple Moving Average (SMA)** indicator.

- A rolling window of recent prices is maintained
- We **BUY** when the current price falls sufficiently below the SMA
- We **SELL** when the current price rises sufficiently above the SMA
- Otherwise, we **HOLD**

---

## Trade-offs & shortcuts

Due to time constraints, several deliberate trade-offs were made:

- Using records instead of classes for database access, which is viable but produces boilerplate
- No advanced trading strategies (e.g. RSI, MACD, order books)
- Limited validation and error handling for edge cases
- No automated testing
- Styling was kept simple and functional rather than polished

---

## Use of external tools & AI

I used an LLM (ChatGPT) during development to:

- Discuss architectural decisions
- Validate trading logic ideas
- Debug frontend state and data flow issues
- Refine UI structure and component separation

All generated suggestions were:
- Manually reviewed
- Manually adapted to project constraints
- Verified by running the application and inspecting behavior
- Critical suggestions were cross-referenced

---

## What I would improve next

- Persisting portfolio snapshots to simplify analytics
- Implement better exception handling and logging
- Add more asset types
- Add multithreading support
- Support multiple types of strategies
- Adding unit and integration tests, especially for trading logic
- Add Docker for easy deployment  