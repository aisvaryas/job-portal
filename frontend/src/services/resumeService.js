import apiClient from "./apiClient";

export const resumeService = {
  // Get all resumes of logged-in user
  getResumes: async () => {
    const response = await apiClient.get("/api/resume/all");
    return response?.data?.data || [];
  },

  // Upload resume
  uploadResume: async (file, isDefault = false, visibility = "PRIVATE") => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("isDefault", String(isDefault));
  formData.append("visibility", visibility);

  return await apiClient.post("/api/resume/upload", formData, {
    headers: { "Content-Type": "multipart/form-data" }
  });
},

  // Delete resume
  deleteResume: async (resumeId) => {
    const response = await apiClient.delete(
      `/api/resume/delete/${resumeId}`
    );

    return response?.data?.data;
  },

  // Get signed download URL
  getResumeDownloadUrl: async (resumeId) => {
    const response = await apiClient.get(
      `/api/resume/${resumeId}`
    );

    return response?.data?.data;
  },

  // Open in new tab
  openResume: async (resumeId) => {
    try {
      const url = await resumeService.getResumeDownloadUrl(resumeId);

      if (url) {
        window.open(url, "_blank");
      }
    } catch (error) {
      console.error("Failed to open resume:", error);
      throw error;
    }
  }
};