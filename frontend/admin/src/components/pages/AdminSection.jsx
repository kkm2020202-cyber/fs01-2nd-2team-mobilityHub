import { useState, useEffect } from "react";
import { User, Mail, Phone, Shield } from "lucide-react";
import "../style/AdminSection.css";
import axios from "axios";

const API_SERVER = "http://localhost:9000";

export default function AdminSection() {
  const [allAdmins, setAllAdmins] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 백엔드에서 관리자 목록 조회
  useEffect(() => {
    const fetchAdmins = async () => {
      try {
        const response = await axios.get(`${API_SERVER}/admin/list`, {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: false, // CORS 크레덴셜 비활성화
        });

        // DB 데이터를 화면에 맞게 변환
        const admins = response.data.map((admin) => ({
          id: admin.adminId,
          adminId: admin.adminId,
          name: admin.adminName,
          area: getAreaFromAdminId(admin.adminId),
          phone: "010-0000-0000",
          email: `${admin.adminId}@smartpark.com`,
          isCurrentUser: admin.adminId === "Padmin",
        }));

        setAllAdmins(admins);
        setLoading(false);
      } catch (err) {
        console.error("관리자 목록 조회 실패:", err);
        
        // 테스트 데이터 (백엔드가 안 되면 이걸로 우선 확인)
        const testAdmins = [
          { id: "Padmin", adminId: "Padmin", name: "주차장 관리자", area: "주차장", phone: "010-1111-1111", email: "padmin@smartpark.com", isCurrentUser: true },
          { id: "Wadmin", adminId: "Wadmin", name: "세차장 관리자", area: "세차장", phone: "010-2222-2222", email: "wadmin@smartpark.com", isCurrentUser: false },
          { id: "Radmin", adminId: "Radmin", name: "정비소 관리자", area: "정비소", phone: "010-3333-3333", email: "radmin@smartpark.com", isCurrentUser: false },
          { id: "Tadmin", adminId: "Tadmin", name: "전체 관리자", area: "전체", phone: "010-4444-4444", email: "tadmin@smartpark.com", isCurrentUser: false },
        ];
        
        setAllAdmins(testAdmins);
        setError("백엔드 서버에 접속할 수 없어 테스트 데이터를 표시합니다.");
        setLoading(false);
      }
    };

    fetchAdmins();
  }, []);

  // 관리자 ID로 구역 판단
  const getAreaFromAdminId = (adminId) => {
    switch (adminId) {
      case "Padmin":
        return "주차장";
      case "Radmin":
        return "정비소";
      case "Tadmin":
        return "전체";
      case "Wadmin":
        return "세차장";
      default:
        return "기타";
    }
  };

  const getAreaClass = (area) => {
    switch (area) {
      case "전체":
        return "tag purple";
      case "입출구":
        return "tag blue";
      case "세차장":
        return "tag green";
      case "정비소":
        return "tag orange";
      case "주차장":
        return "tag indigo";
      case "통계 분석":
        return "tag pink";
      default:
        return "tag gray";
    }
  };

  if (loading) return <div>로딩 중...</div>;

  return (
    <div className="admin-section">
      {error && <div className="error-message">{error}</div>}
      
      <h1>관리자 관리</h1>
      <p className="admin-count">총 {allAdmins.length}명의 관리자</p>

      <table className="admin-table">
        <thead>
          <tr>
            <th>관리자 ID</th>
            <th>이름</th>
            <th>관리 구역</th>
            <th>연락처</th>
            <th>이메일</th>
          </tr>
        </thead>
        <tbody>
          {allAdmins.map((admin) => (
            <tr key={admin.id} className={admin.isCurrentUser ? "current-user" : ""}>
              <td>
                {admin.isCurrentUser && <span className="badge">현재 사용자</span>}
                {admin.adminId}
              </td>
              <td>{admin.name}</td>
              <td>
                <span className={getAreaClass(admin.area)}>{admin.area}</span>
              </td>
              <td>{admin.phone}</td>
              <td>{admin.email}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
