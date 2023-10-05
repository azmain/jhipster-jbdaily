export interface IMoneyExchange {
  id: number;
  name?: string | null;
  digit?: string | null;
  link?: string | null;
  shortName?: string | null;
}

export type NewMoneyExchange = Omit<IMoneyExchange, 'id'> & { id: null };
