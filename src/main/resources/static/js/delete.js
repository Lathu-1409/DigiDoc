document.getElementById("deleteSearchForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const queryParams = new URLSearchParams({
      fileName: document.getElementById("fileName").value,
      subjectName: document.getElementById("subjectName").value,
      category: document.getElementById("category").value,
      uploadedByUsername: document.getElementById("uploadedByUsername").value
  });

  try {
      const response = await fetch(`/documents/query?${queryParams}`);
      if (response.ok) {
          const documents = await response.json();

          const tableBody = document.querySelector(".results-table tbody");
          tableBody.innerHTML = ""; // Clear previous results

          if (documents.length === 0) {
              const row = document.createElement("tr");
              row.innerHTML = `<td colspan="6">No documents found matching the search criteria.</td>`;
              tableBody.appendChild(row);
              document.querySelector(".results-table").classList.remove("hidden");
              return;
          }

          documents.forEach((doc) => {
              const row = document.createElement("tr");
              row.innerHTML = `
                  <td>${doc.fileName || "N/A"}</td>
                  <td>${doc.subjectName || "N/A"}</td>
                  <td>${doc.category || "N/A"}</td>
                  <td>${doc.semester || "N/A"}</td>
                  <td>${doc.uploadDate || "N/A"}</td>
                  <td><button class="btn btn-danger delete-btn" data-id="${doc.id}">Delete</button></td>
              `;
              tableBody.appendChild(row);
          });

          document.querySelector(".results-table").classList.remove("hidden");

          // Attach event listeners to delete buttons
          const deleteButtons = document.querySelectorAll(".delete-btn");
          deleteButtons.forEach((button) => {
              button.addEventListener("click", async function () {
                  const documentId = this.getAttribute("data-id");
                  const confirmation = confirm("Are you sure you want to delete this document?");
                  if (confirmation) {
                      try {
                          const deleteResponse = await fetch(`/documents/${documentId}`, {
                              method: "DELETE"
                          });
                          if (deleteResponse.ok) {
                              alert("Document deleted successfully!");
                              this.closest("tr").remove(); // Remove the deleted row from the table
                          } else {
                              const error = await deleteResponse.text();
                              alert("Error deleting document: " + error);
                          }
                      } catch (err) {
                          alert("Failed to delete document: " + err.message);
                      }
                  }
              });
          });
      } else {
          const error = await response.text();
          alert("Error fetching documents: " + error);
      }
  } catch (err) {
      alert("Failed to fetch documents: " + err.message);
  }
});