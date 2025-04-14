document.getElementById("bySemesterForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const semester = document.getElementById("semester").value;
  const count = document.getElementById("count").value;

  try {
      const response = await fetch(`/documents/by-sem/${semester}/${count}`);
      if (response.ok) {
          const data = await response.json();

          const tableBody = document.querySelector(".results-table tbody");
          tableBody.innerHTML = ""; // Clear previous results

          if (data.length === 0) {
              const row = document.createElement("tr");
              row.innerHTML = `<td colspan="5">No documents found for the selected semester.</td>`;
              tableBody.appendChild(row);
              document.querySelector(".results-table").classList.remove("hidden");
              return;
          }

          data.forEach((doc) => {
              const row = document.createElement("tr");
              row.innerHTML = `
                  <td>${doc.fileName || "N/A"}</td>
                  <td>${doc.subjectName || "N/A"}</td>
                  <td>${doc.category || "N/A"}</td>
                  <td>${doc.uploadDate || "N/A"}</td>
                  <td><a href="/documents/download/${doc.id}" target="_blank">Download</a></td>
              `;
              tableBody.appendChild(row);
          });

          document.querySelector(".results-table").classList.remove("hidden");
      } else {
          const error = await response.text();
          alert("Error fetching documents by semester: " + error);
      }
  } catch (err) {
      alert("Failed to fetch documents: " + err.message);
  }
});