export interface IDivision {
  id: number;
  name?: string | null;
  bnName?: string | null;
}

export type NewDivision = Omit<IDivision, 'id'> & { id: null };
