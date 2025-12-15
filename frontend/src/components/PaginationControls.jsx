export default function PaginationControls({
                                               currentPage,
                                               hasPrev,
                                               hasNext,
                                               onPrev,
                                               onNext,
                                           }) {
    return (
        <div style={{ marginTop: 16, display: 'flex', alignItems: 'center', gap: 12 }}>
            <button onClick={onPrev} disabled={!hasPrev}>
                Prev
            </button>

            <div>
                Page <strong>{currentPage + 1}</strong>
            </div>

            <button onClick={onNext} disabled={!hasNext}>
                Next
            </button>
        </div>
    );
}