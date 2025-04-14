document.getElementById("downloadForm").addEventListener("submit", async function (event) {
    event.preventDefault();
  
    const documentId = document.getElementById("documentId").value;
    try {
      const response = await fetch(`/documents/download/${documentId}`);
      if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "document.pdf";
        a.click();
      } else {
        const error = await response.text();
        alert("Error downloading document: " + error);
      }
    } catch (err) {
      alert("Failed to download document: " + err.message);
    }
  });