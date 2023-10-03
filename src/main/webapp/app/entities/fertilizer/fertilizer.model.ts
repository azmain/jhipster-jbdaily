export interface IFertilizer {
  id: number;
  name?: string | null;
  bnName?: string | null;
  accountNo?: string | null;
  accountTitle?: string | null;
}

export type NewFertilizer = Omit<IFertilizer, 'id'> & { id: null };
