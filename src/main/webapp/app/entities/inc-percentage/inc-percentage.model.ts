export interface IIncPercentage {
  id: number;
  name?: string | null;
}

export type NewIncPercentage = Omit<IIncPercentage, 'id'> & { id: null };
