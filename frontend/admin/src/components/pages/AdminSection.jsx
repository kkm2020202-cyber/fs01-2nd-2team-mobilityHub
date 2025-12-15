import { User, Mail, Phone, Shield } from "lucide-react";
import "../style/AdminSection.css";

export default function AdminSection() {
  const allAdmins = [
    {
      id: "1",
      adminId: "admin001",
      name: "관리자",
      area: "전체",
      phone: "010-1234-5678",
      email: "admin@smartpark.com",
      isCurrentUser: true,
    },
    {
      id: "2",
      adminId: "admin002",
      name: "김입구",
      area: "입출구",
      phone: "010-2345-6789",
      email: "entrance@smartpark.com",
    },
    {
      id: "3",
      adminId: "admin003",
      name: "이세차",
      area: "세차장",
      phone: "010-3456-7890",
      email: "carwash@smartpark.com",
    },
    {
      id: "4",
      adminId: "admin004",
      name: "박정비",
      area: "정비존",
      phone: "010-4567-8901",
      email: "maintenance@smartpark.com",
    },
    {
      id: "5",
      adminId: "admin005",
      name: "최주차",
      area: "주차장",
      phone: "010-5678-9012",
      email: "parking@smartpark.com",
    },
    {
      id: "6",
      adminId: "admin006",
      name: "정통계",
      area: "통계 분석",
      phone: "010-6789-0123",
      email: "stats@smartpark.com",
    },
  ];

  const getAreaClass = (area) => {
    switch (area) {
      case "전체":
        return "tag purple";
      case "입출구":
        return "tag blue";
      case "세차장":
        return "tag green";
      case "정비존":
        return "tag orange";
      case "주차장":
        return "tag indigo";
      case "통계 분석":
        return "tag pink";
      default:
        return "tag gray";
    }
  };

  return (
    <div className="admin-wrapper">
      <div className="admin-box">
        <div className="admin-header">
          <h3>전체 관리자 목록</h3>
          <p>총 {allAdmins.length}명의 관리자</p>
        </div>

        <div className="table-container">
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
                <tr key={admin.id} className={admin.isCurrentUser ? "highlight-row" : ""}>
                  <td>
                    <div className="id-cell">
                      {admin.isCurrentUser && <Shield className="icon-shield" />}
                      {admin.adminId}
                    </div>
                  </td>

                  <td>
                    <div className="name-cell">
                      <div className={`avatar ${admin.isCurrentUser ? "main" : ""}`}>
                        <User className="avatar-icon" />
                      </div>
                      {admin.name}
                    </div>
                  </td>

                  <td>
                    <span className={getAreaClass(admin.area)}>{admin.area}</span>
                  </td>

                  <td>
                    <div className="info-cell">
                      <Phone className="info-icon" />
                      {admin.phone}
                    </div>
                  </td>

                  <td>
                    <div className="info-cell">
                      <Mail className="info-icon" />
                      {admin.email}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
