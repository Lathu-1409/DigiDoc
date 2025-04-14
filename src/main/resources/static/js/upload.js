document.getElementById("uploadForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const formData = new FormData();
  formData.append("fileName", document.getElementById("fileName").value);
  formData.append("subjectName", document.getElementById("subjectName").value);
  formData.append("category", document.getElementById("category").value);
  formData.append("sem", document.getElementById("semester").value);
  formData.append("uploadedByUsername", document.getElementById("uploadedBy").value);
  formData.append("file", document.getElementById("file").files[0]);

  const uploadStatus = document.getElementById("uploadStatus");
  uploadStatus.classList.remove("hidden");
  uploadStatus.querySelector(".status-text").textContent = "Uploading...";

  try {
    const response = await fetch("/documents/upload", {
      method: "POST",
      body: formData,
    });

    if (response.ok) {
      uploadStatus.querySelector(".status-text").textContent = "Document uploaded successfully!";
      console.log("Upload successful!");
    } else {
      const error = await response.text();
      uploadStatus.querySelector(".status-text").textContent = "Error uploading document.";
      console.error("Error response:", error);
    }
  } catch (err) {
    uploadStatus.querySelector(".status-text").textContent = "Failed to upload document.";
    console.error("Upload failed:", err.message);
  }
});