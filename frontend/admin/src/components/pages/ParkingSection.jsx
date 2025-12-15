import React from "react";
import "../style/ParkingSection.css";

const PARKING_IMAGE =
  "https://images.unsplash.com/photo-1653750366046-289780bd8125?auto=format&fit=crop&w=1000&q=80";

export default function ParkingSection() {
  return (
    <div className="parking-section">
      {/* 통계 카드 */}
      <div className="parking-stats-grid">
        <div className="parking-stat-card placeholder-box">
          <div className="placeholder-text">데이터 들어갈 자리</div>
        </div>

        <div className="parking-stat-card placeholder-box">
          <div className="placeholder-text">데이터 들어갈 자리</div>
        </div>

        <div className="parking-stat-card placeholder-box">
          <div className="placeholder-text">데이터 들어갈 자리</div>
        </div>

        <div className="parking-stat-card placeholder-box">
          <div className="placeholder-text">데이터 들어갈 자리</div>
        </div>
      </div>

      {/* 카메라 + 주차 목록 */}
      <div className="parking-content-grid">
        {/* 카메라 영역 */}
        <div className="parking-camera-container placeholder-box">
          <div className="placeholder-text">데이터 들어갈 자리</div>
        </div>

        {/* 오른쪽 리스트 영역 */}
        <div className="parking-list placeholder-box">
          <div className="placeholder-text">데이터 들어갈 자리</div>
        </div>
      </div>
    </div>
  );
}
