import React from 'react';
import './Pagination.css';

const Pagination = ({ page, totalPages, onPageChange }) => {
  if (totalPages <= 1) return null;

  const pages = [];
  const maxVisible = 5;
  let startPage = Math.max(0, page - Math.floor(maxVisible / 2));
  let endPage = Math.min(totalPages - 1, startPage + maxVisible - 1);

  if (endPage - startPage < maxVisible - 1) {
    startPage = Math.max(0, endPage - maxVisible + 1);
  }

  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }

  return (
    <div className="pagination">
      <button
        onClick={() => onPageChange(page - 1)}
        disabled={page === 0}
        className="pagination-btn"
      >
        Previous
      </button>
      {startPage > 0 && (
        <>
          <button onClick={() => onPageChange(0)} className="pagination-btn">1</button>
          {startPage > 1 && <span className="pagination-ellipsis">...</span>}
        </>
      )}
      {pages.map((p) => (
        <button
          key={p}
          onClick={() => onPageChange(p)}
          className={`pagination-btn ${p === page ? 'active' : ''}`}
        >
          {p + 1}
        </button>
      ))}
      {endPage < totalPages - 1 && (
        <>
          {endPage < totalPages - 2 && <span className="pagination-ellipsis">...</span>}
          <button onClick={() => onPageChange(totalPages - 1)} className="pagination-btn">
            {totalPages}
          </button>
        </>
      )}
      <button
        onClick={() => onPageChange(page + 1)}
        disabled={page >= totalPages - 1}
        className="pagination-btn"
      >
        Next
      </button>
    </div>
  );
};

export default Pagination;
