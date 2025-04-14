document.getElementById("mostViewedForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const count = document.getElementById("count").value;

  try {
      const response = await fetch(`/documents/most-viewed/${count}`);
      if (response.ok) {
          const data = await response.json();

          const tableBody = document.querySelector(".results-table tbody");
          tableBody.innerHTML = ""; // Clear previous results

          if (data.length === 0) {
              const row = document.createElement("tr");
              row.innerHTML = `<td colspan="5">No documents found.</td>`;
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
                  <td>${doc.viewCount || 0}</td>
                  <td><a href="/documents/download/${doc.id}" target="_blank">Download</a></td>
              `;
              tableBody.appendChild(row);
          });

          document.querySelector(".results-table").classList.remove("hidden");
      } else {
          const error = await response.text();
          alert("Error fetching most viewed documents: " + error);
      }
  } catch (err) {
      alert("Failed to fetch documents: " + err.message);
  }
});