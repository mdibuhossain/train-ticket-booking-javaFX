import axios from "axios";
import cx from "classnames";
import React from "react";

const BusModel = () => {
  const [seats, setSeats] = React.useState([]);
  React.useEffect(() => {
    axios.get("./data/seats.json").then(({ data }) => setSeats(data));
  }, []);
  const seatHandler = (ev) => {
    const tmpSeats = [...seats];
    tmpSeats.map((tmp) => {
      if (tmp.name === ev.name && !tmp.isBooked) {
        tmp.isSelected = !tmp.isSelected;
      }
    });
    setSeats(tmpSeats);
  };
  return (
    <>
      <div className="w-60 bg-yellow-400 m-5 p-5 rounded-md grid gap-3 grid-cols-5">
        {seats.map((seat, index) => (
          <>
            {seat?.name?.includes("3") && <span>&nbsp;</span>}
            <IndividualSeat key={index} seat={seat} seatHandler={seatHandler} />
          </>
        ))}
      </div>
    </>
  );
};

const IndividualSeat = ({ seat, seatHandler }) => {
  return (
    <button
      type="button"
      className={cx(
        "grid",
        "place-items-center",
        "font-semibold",
        "rounded-md",
        "text-gray-950",
        "bg-white",
        "border-2",
        "hover:border-gray-950",
        "duration-150",
        "ease-in-out",
        {
          "bg-indigo-400": seat?.isSelected,
          "border-gray-950": seat?.isSelected,
          "bg-red-400": seat?.isBooked,
          "border-red-400": seat?.isBooked,
          "hover:border-red-400": seat?.isBooked,
        }
      )}
      onClick={() => seatHandler(seat)}
    >
      {seat.name}
    </button>
  );
};

export default BusModel;
