export interface AiRecommendation {
  id: number;
  internship: {
    id: number;
    title: string;
    description?: string;
    company: { name: string };
    technologies: { id: number; name: string }[];
  };
  score: number;
  explanation: string;
  createdAt: string;
}