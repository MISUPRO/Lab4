<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.hibernate.*, org.hibernate.cfg.Configuration, java.util.*, model.RegionEntity" %>
<%
    SessionFactory factory = new Configuration().configure().buildSessionFactory();
    Session hibSession = factory.openSession();
    List<RegionEntity> regions = hibSession.createQuery("FROM RegionEntity", RegionEntity.class).list();
    hibSession.close();
    factory.close();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen p-8">
<div class="max-w-4xl mx-auto bg-white rounded-xl shadow-md p-6">
    <h1 class="text-3xl font-bold text-gray-800 mb-4">Welcome, Admin: ${sessionScope.userName}</h1>
    <p class="text-lg text-gray-600 mb-6">You are viewing the region data.</p>

    <h2 class="text-2xl font-semibold text-gray-700 mb-4">Region Table</h2>
    <div class="overflow-x-auto">
        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-200 text-gray-600">
            <tr>
                <th class="py-3 px-6 text-left border-b">ID</th>
                <th class="py-3 px-6 text-left border-b">Region Name</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (RegionEntity region : regions) {
            %>
            <tr class="hover:bg-gray-100">
                <td class="py-3 px-6 border-b"><%= region.getId_region() %></td>
                <td class="py-3 px-6 border-b"><%= region.getRegion_name() %></td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
